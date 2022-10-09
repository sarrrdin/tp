package nus.climods.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import nus.climods.commons.core.index.Index;
import nus.climods.commons.core.module.ModuleCode;
import nus.climods.commons.util.StringUtil;
import nus.climods.logic.parser.exceptions.ParseException;
import nus.climods.model.person.Address;
import nus.climods.model.person.Email;
import nus.climods.model.person.Name;
import nus.climods.model.person.Phone;
import nus.climods.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Returns List of whitespace-delimited arguments given arguments string supplied by the user
     * @param arguments String supplied by user as arguments after preamble
     * @return List of arguments provided
     */
    public static List<String> convertArgumentStringToList(String arguments) {
        List<String> res = ArgumentTokenizer
                .tokenize(arguments.trim(), new Prefix(""))
                .getAllValues(new Prefix(""));

        // When arguments is empty string once trimmed, res is a List with just one empty string
        // Return empty list to demarcate this case clearly
        if (res.size() == 1 && res.get(0).trim().equals("")) {
            return List.of();
        }

        return res;
    }

    /**
     * Parses a {@code String} and returns Optional of a {@code ModuleCode} if supplied string represents a module code
     * in full list. Returns empty Optional otherwise
     * @param listModuleCode String representing input from user for a list module code
     * @return Optional of ModuleCode if supplied string valid or empty optional otherwise
     */
    public static Optional<ModuleCode> parseListModuleCode(String listModuleCode) {
        // TODO: check if ModuleCode is in full list of module codes
        return Optional.of(new ModuleCode(listModuleCode));
    }

    /**
     * Parses a {@code String} and returns Optional of a {@code ModuleCode} if supplied string represents a module code
     * in user's list. Returns empty Optional otherwise
     * @param listModuleCode String representing input from user for a user module code
     * @return Optional of ModuleCode if supplied string valid or empty optional otherwise
     */
    public static Optional<ModuleCode> parseUserModuleCode(String listModuleCode) {
        // TODO: check if ModuleCode is in user's list of module codes
        return Optional.of(new ModuleCode(listModuleCode));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
